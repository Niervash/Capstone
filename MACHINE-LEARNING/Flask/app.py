from flask import Flask, request, jsonify
import io
from tensorflow import keras
import os
import numpy as np
from keras.preprocessing.image import img_to_array, load_img

app = Flask(__name__)


model_cat = keras.models.load_model("Cat_model.h5")

label_cat = ["earmites", "konjungtivitis"]


def predict_animal(model, label, request_files_key):
    if request.method == "POST":
        if request_files_key not in request.files:
            return jsonify({"error": "no file"})

        file = request.files[request_files_key]
        if file.filename == "":
            return jsonify({"error": "no file"})

        img = file.read()  # Read the file data
        img = load_img(io.BytesIO(img), target_size=(224, 224))
        x = img_to_array(img) / 255.0
        x = np.expand_dims(x, axis=0)
        images = np.vstack([x])

        classes = model.predict(images, batch_size=10)
        prediction = classes[0][0]
        confidence = round(float(prediction), 2)
        image = file.filename

        if prediction > 0.5:
            result = label[0]
        else:
            result = label[1]

    return jsonify({"prediction": result, 
                    "confidence": confidence, 
                    "Picture": image})

@app.route("/")
def index():
    return "Hello World"

@app.route("/predict_cat", methods=['POST'])
def predict_cat():
    return predict_animal(model_cat, label_cat, "Kucing")



if __name__ == "__main__":
    app.run(host='0.0.0.0', port=int(os.environ.get("PORT", 8080)), debug=True)