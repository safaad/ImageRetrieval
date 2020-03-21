import os 
  


for filename in os.listdir(".\\train2014"):
    line=filename.split("COCO_train2014_")
    s=line[1].lstrip("0") 
    dst =".\\train2014\\" +s
    src =".\\train2014\\"+ filename 
    print(filename)
        
    # rename() function will 
    # rename all the files 
    os.rename(src, dst)
    
# for filename in os.listdir(".\\val2014"):
#     line=filename.split("COCO_val2014_")
#     s=line[1].lstrip("0") 
#     dst =".\\val2014\\" +s
#     src =".\\val2014\\"+ filename 
#     print(filename)
        
#     # rename() function will 
#     # rename all the files 
#     os.rename(src, dst)  