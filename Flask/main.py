from flask import Flask, request, jsonify
import io
from tensorflow import keras
import numpy as np
from keras.preprocessing.image import img_to_array, load_img
import urllib.request

app = Flask(__name__)

# Download the Cat_model.h5 file
cat_model_url = "https://storage.googleapis.com/hugging-pet_cloudbuild/Cat_model.h5"
cat_model_path = "/Cat_model.h5"
urllib.request.urlretrieve(cat_model_url, cat_model_path, reporthook=lambda blocknum, blocksize, totalsize: print(f"Downloaded Cat_model {blocknum * blocksize / totalsize * 100:.2f}%"))

# Download the Dog_model.h5 file
dog_model_url = "https://storage.googleapis.com/hugging-pet_cloudbuild/Dog_model.h5"
dog_model_path = "/Dog_model.h5"
urllib.request.urlretrieve(dog_model_url, dog_model_path, reporthook=lambda blocknum, blocksize, totalsize: print(f"Downloaded Dog_model {blocknum * blocksize / totalsize * 100:.2f}%"))

# Load the models
model_cat = keras.models.load_model(cat_model_path)
model_dog = keras.models.load_model(dog_model_path)

# Labels for models
label_cat = ["earmites", "konjungtivitis"]
label_dog = ["ringworm", "konjungtivitis"]


def preprocess_image(file):
    img = file.read()
    img = load_img(io.BytesIO(img), target_size=(224, 224))
    img_array = img_to_array(img)
    img_array = img_array / 255.0  # Normalize pixel values
    img_array = np.expand_dims(img_array, axis=0)
    return img_array


def predict_animal(model, label, request_files_key):
    if request.method == "POST":
        if request_files_key not in request.files:
            return jsonify({"error": "no file"})

        file = request.files[request_files_key]
        if file.filename == "":
            return jsonify({"error": "no file"})

        img_array = preprocess_image(file)

        classes = model.predict(img_array, batch_size=1)
        image = file.filename

        if classes[0] > 0.5:
            result = image + " - " + label[0]
        else:
            result = image + " - " + label[1]

        return jsonify({"prediction": result})


@app.route("/predict_cat", methods=['POST'])
def predict_cat():
    return predict_animal(model_cat, label_cat, "gambar_cat")


@app.route("/predict_dog", methods=['POST'])
def predict_dog():
    return predict_animal(model_dog, label_dog, "gambar_dog")


if __name__ == "__main__":
    app.run(debug=True)
