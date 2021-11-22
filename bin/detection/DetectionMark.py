import numpy as np
import os
import six.moves.urllib as urllib
import sys
import tensorflow as tf

from PIL import Image

sys.path.append("..")
from object_detection.utils import ops as utils_ops

config = tf.ConfigProto()
config.gpu_options.allow_growth = True
session = tf.Session(config=config)

from object_detection.utils import label_map_util
from object_detection.utils import visualization_utils as vis_util

# 학습모델 경로
PATH_TO_FROZEN_GRAPH = 'src/detection/model/output_inference_graph_v1/frozen_inference_graph.pb'
# Label Map 경로
# PATH_TO_LABELS = os.path.join(os.getcwd(), './src/detection/model/label_map.pdtxt')
PATH_TO_LABELS = 'src/detection/model/label_map.pdtxt'

print("model load")
print(PATH_TO_LABELS)
print(PATH_TO_FROZEN_GRAPH)

detection_graph = tf.Graph()
with detection_graph.as_default():
    od_graph_def = tf.GraphDef()
    with tf.gfile.GFile(PATH_TO_FROZEN_GRAPH, 'rb') as fid:
        serialized_graph = fid.read()
        od_graph_def.ParseFromString(serialized_graph)
        tf.import_graph_def(od_graph_def, name='')

print("labelmap load")
category_index = label_map_util.create_category_index_from_labelmap(PATH_TO_LABELS, use_display_name=True)

def load_image_into_numpy_array(image):
  (im_width, im_height) = image.size
  return np.array(image.getdata()).reshape(
      (im_height, im_width, 3)).astype(np.uint8)

def run_inference_for_single_image(image, graph):
    with graph.as_default():
        with tf.Session() as sess:
            # Get handles to input and output tensors
            ops = tf.get_default_graph().get_operations()
            all_tensor_names = {output.name for op in ops for output in op.outputs}
            tensor_dict = {}
            for key in [
              'num_detections', 'detection_boxes', 'detection_scores',
              'detection_classes', 'detection_masks'
            ]:
                tensor_name = key + ':0'
                if tensor_name in all_tensor_names:
                    tensor_dict[key] = tf.get_default_graph().get_tensor_by_name(tensor_name)

            if 'detection_masks' in tensor_dict:
                # The following processing is only for single image
                detection_boxes = tf.squeeze(tensor_dict['detection_boxes'], [0])
                detection_masks = tf.squeeze(tensor_dict['detection_masks'], [0])
                # Reframe is required to translate mask from box coordinates to image coordinates and fit the image size.
                real_num_detection = tf.cast(tensor_dict['num_detections'][0], tf.int32)
                detection_boxes = tf.slice(detection_boxes, [0, 0], [real_num_detection, -1])
                detection_masks = tf.slice(detection_masks, [0, 0, 0], [real_num_detection, -1, -1])
                detection_masks_reframed = utils_ops.reframe_box_masks_to_image_masks(
                    detection_masks, detection_boxes, image.shape[0], image.shape[1])
                detection_masks_reframed = tf.cast(
                    tf.greater(detection_masks_reframed, 0.5), tf.uint8)
                # Follow the convention by adding back the batch dimension
                tensor_dict['detection_masks'] = tf.expand_dims(
                    detection_masks_reframed, 0)
            image_tensor = tf.get_default_graph().get_tensor_by_name('image_tensor:0')

            # Run inference
            output_dict = sess.run(tensor_dict,
                                 feed_dict={image_tensor: np.expand_dims(image, 0)})

            # all outputs are float32 numpy arrays, so convert types as appropriate
            output_dict['num_detections'] = int(output_dict['num_detections'][0])
            output_dict['detection_classes'] = output_dict['detection_classes'][0].astype(np.uint8)
            output_dict['detection_boxes'] = output_dict['detection_boxes'][0]
            output_dict['detection_scores'] = output_dict['detection_scores'][0]
            if 'detection_masks' in output_dict:
                output_dict['detection_masks'] = output_dict['detection_masks'][0]
    return output_dict

# 인증마크 탐지 결과 딕셔너리에서 클래스 이름을 얻어옴
def getClassName(output_dict):
    num = 0
    for score in output_dict["detection_scores"]:
        if(score >= 0.5):
            num+=1;

    classes = []
    for n in range(num):
        class_name = output_dict['detection_classes'][n]

        if(class_name == 1) :
            c_name = 'korCertifiMark (국내 건강기능식품)'
        elif(class_name == 2) :
            c_name = 'gmp (국내 GMP)'
        elif(class_name == 3) :
            c_name = 'usda (미국 유기농)'
        elif(class_name == 4) :
            c_name = 'usp (미국 USP)'
        elif(class_name == 5) :
            c_name = 'aco (호주 유기농)'
        
        if c_name in classes:
            continue
        else:
            classes.append(c_name)
    
    # 정렬 ( 오름차순 )
    classes.sort()
    
    return classes

def create_white_matrix(width, height):
    mat = None
    
    mat = np.ones((height, width, 3), dtype = "uint8") * 255
    
    return mat

def input_image_to_white_matrix(target, _width, _height, x=0, y=0):
    result_matrix = create_white_matrix(_width, _height)
    width, height, channel = target.shape
    
    if(width > _width or height > _height):
        return None
    
    result_matrix[x:x+width, y:y+height] = target
    
    return result_matrix

print("image open")
image = Image.open("src/detection/test.jpg")
image_np = load_image_into_numpy_array(image)

height, width, channel = image_np.shape

print(width)

# 입력 이미지 전처리 ( 해상도 변경 ( 1400 x 1400 ))
image_np = input_image_to_white_matrix(image_np, 1400, 1400)

height, width, channel = image_np.shape
print(width)

"""
# 인증마크 탐지 결과 얻기
output_dict = run_inference_for_single_image(image_np, detection_graph)
print("success detection")

class_name = getClassName(output_dict)

print(class_name[0])
"""