import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

import io
import tensorflow
from tensorflow import keras
import numpy as np
from PIL import Image
from flask import Flask, request, jsonify

#Load model.h5 pake keras
model = keras.models.load_model("model__v3.h5")
#Label yang ada di dalam model__v3.h5
label = ["earmites", "konjungtivitis"]

app = Flask(__name__)

#Function yg berfungsi untuk melakukan prediksi pada gambar yang di input
def predict_label(img):
    i = np.asanyarray(img)/255.0
    #sesuaikan dengan input_size yang digunakan pada saat pembuatan model
    i = i.reshape(224, 224, 3)
    pred = model.predict(i)
    result = label[np.argmax(pred)]
    return result

@app.route("/predict", methods=["GET", "POST"])
def index():
    file = request.files.get('file')
    if file is None or file.filename == "":
        return jsonify({"error": "no file"})
    
    image_bytes = file.read()
    img = Image.open(io.BytesIO(image_bytes))
    img = img.resize((224,224), Image.NEAREST)
    pred_img = predict_label(img)
    return pred_img

if __name__ == "__main__":
    app.run(debug=True)
    