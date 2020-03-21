from show_and_tell import show_tell
import sys
from py4j.java_gateway import JavaGateway


global gateway
gateway = JavaGateway()   
global lucene
lucene= gateway.entry_point   

class SearchIndex:
    
    def SearchIndexByCaption(self,imagepath):
        self.tell = show_tell()
        caption = self.tell.getCaption(imagepath)
        print("Caption:"+caption)
        similarity=lucene.search(caption)
        return similarity,caption

    def SearchIndexByText(self,query):
        similarity=lucene.search(query)
        return similarity

    
