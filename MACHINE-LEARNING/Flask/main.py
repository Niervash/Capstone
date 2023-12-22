from flask import Flask, request, jsonify
import io
from tensorflow import keras
import numpy as np
from keras.preprocessing.image import img_to_array, load_img
import urllib.request

app = Flask(__name__)

import os
from flask import Flask, request, jsonify
import io
from tensorflow import keras
import numpy as np
from keras.preprocessing.image import img_to_array, load_img
import urllib.request

app = Flask(__name__)

# Define the paths for the model files
cat_model_path = "Cat_model.h5"
dog_model_path = "Dog_model.h5"

# Check if the Cat_model.h5 file exists in the directory
if not os.path.exists(cat_model_path):
    # Download the Cat_model.h5 file
    cat_model_url = "https://storage.googleapis.com/hugging-pet_cloudbuild/Cat_model.h5"
    urllib.request.urlretrieve(cat_model_url, cat_model_path, reporthook=lambda blocknum, blocksize, totalsize: print(f"Downloaded Cat_model {blocknum * blocksize / totalsize * 100:.2f}%"))
else:
    print("file avaibll")
# Check if the Dog_model.h5 file exists in the directory
if not os.path.exists(dog_model_path):
    # Download the Dog_model.h5 file
    dog_model_url = "https://storage.googleapis.com/hugging-pet_cloudbuild/Dog_model.h5"
    urllib.request.urlretrieve(dog_model_url, dog_model_path, reporthook=lambda blocknum, blocksize, totalsize: print(f"Downloaded Dog_model {blocknum * blocksize / totalsize * 100:.2f}%"))
else:
    print("file avaibll")

label_cat = ["earmites", "konjungtivitis"]
label_dog = ["ringworm", "konjungtivitis"]

# Load the models
model_cat = keras.models.load_model(cat_model_path)
model_dog = keras.models.load_model(dog_model_path)


# model_cat = keras.models.load_model("https://storage.googleapis.com/hugging-pet_cloudbuild/Cat_model.h5")
# model_dog = keras.models.load_model("Dog_model.h5")


label_cat = ["earmites", "konjungtivitis"]
label_dog = ["ringworm", "konjungtivitis"]


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

    return jsonify({"prediction": result, "confidence": confidence, "Picture": image})


@app.route("/predict_cat", methods=['POST'])
def predict_cat():
    return predict_animal(model_cat, label_cat, "Kucing")


@app.route("/predict_dog", methods=['POST'])
def predict_dog():
    return predict_animal(model_dog, label_dog, "Anjing")


if __name__ == "__main__":
    app.run(debug=True)