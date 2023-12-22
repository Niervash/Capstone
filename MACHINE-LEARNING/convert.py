import tensorflowjs as tfjs
from tensorflow.keras.models import load_model


def convert_to_json(model_path, output_dir):
    model = load_model(model_path)
    tfjs.converters.save_keras_model(model, output_dir)
    print(f"Model {model_path} berhasil dikonversi ke format JSON di {output_dir}")

if __name__ == '__main__':
    model_path = "model_v1.h5"
    output_dir = "converted_model"
    convert_to_json(model_path, output_dir)
