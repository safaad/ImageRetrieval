from model import ShowAndTellModel
from PIL import Image
import numpy as np
import argparse
import sys
import os
import timeit
class show_tell:
    
    def create_arg_parser():
        parser = argparse.ArgumentParser(description='Image Captioning')
        parser.add_argument('image',
                        help='Path to the image directory.')
        return parser


    def getCaption(self,imagepath):
        answere = ""
        print("image")
        if os.path.exists(imagepath):
            print("File exist")
        
            image = Image.open(imagepath)
            image = np.array(image)
            state = model.feed_image(image)
            cur_token = self.word2token[self.start_token]
            end = self.word2token[self.end_token]
            
            for i in range(20):
                if cur_token == end:
                    break
                t = np.array([cur_token])
                softmax,state,_ = model.inference_step(t,state)
                cur_token = np.argmax(softmax)
                if cur_token == self.word2token[self.end_token]:
                    break
                answere += self.token2word[cur_token]+" "
        else:
            print("File Not Found {}".format(parsed_args.image))
        return answere

    def __init__(self):
        print("hi")

        start2=timeit.default_timer()
        with open("dictionary.txt") as f:
            lines = f.read().split("\n")
    
        self.word2token = {}
        self.token2word = {}
        for line in lines[:-1]:
            l = line.split('    ')
            word = l[0]
            token = int(l[1])
            self.word2token[word] = token
            self.token2word[token] = word
            
        global model
        model = ShowAndTellModel('optimized.pb')
        self.start_token="<S>"
        self.end_token="</S>"
        stop2=timeit.default_timer()

        print("Tokenizer time:", stop2-start2)

        
        
        
        
        
        
    