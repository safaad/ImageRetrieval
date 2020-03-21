import csv
import os
def main():
    with open('.\\train.csv') as csvfile:
        readCSV = csv.reader(csvfile, delimiter=",")
        im=""
        text=""
        i=0
        op=None
        for row in readCSV:
          if os.path.exists(".\\train2014\\"+row[0]):
                    
            i+=1
            im2=row[0]
            print(im2)
          
            text=row[1]
            if im2 != im:
                im=im2
                if op != None:
                    op.close()
                op = open(".\\Docs\\"+im.rstrip('.jpg')+".txt",'w+')
                op.write(text+"\n")
            else:
                op.write(text+"\n")

if __name__=="__main__":
    main()
        