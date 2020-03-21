from search import SearchIndex
from show_and_tell import show_tell
import sys
from py4j.java_gateway import JavaGateway
import sys
import os
import timeit

def SearchByCaption(imgpath):
    start2=timeit.default_timer()
    obj=SearchIndex()
    
    if os.path.exists(imgpath):
        results,caption=obj.SearchIndexByCaption(imgpath)
        print(*results)
        imgres=list()
        scores=list()
        
        for result in results:
            line=result.split('\t')
            img=line[0].split("Docs\\")
            imgres.append('/static/img/results/'+img[1].replace('.txt','.jpg'))
            scores.append(line[1])
        return caption, imgres, scores
    stop2=timeit.default_timer()

    print("Total time:", stop2-start2)
    return None

def SearchByText(query):
    start2=timeit.default_timer()
    obj=SearchIndex()

    results=obj.SearchIndexByText(query)
    print(*results)
    imgres=list()
    scores=list()
    for result in results:
        line=result.split('\t')
        img=line[0].split("Docs\\")
        imgres.append('/static/img/results/'+img[1].replace('.txt','.jpg'))
        scores.append(line[1])
    stop2=timeit.default_timer()

    print("Total time:", stop2-start2)

    return  imgres, scores
