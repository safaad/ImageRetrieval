from flask import Flask,render_template, request
from MainSearch import SearchByCaption, SearchByText
from werkzeug import secure_filename
import os

APP_ROOT = os.path.dirname(os.path.abspath(__file__))
UPLOAD_FOLDER = os.path.join(APP_ROOT, 'static', 'queries')


app = Flask(__name__)


app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
@app.route('/')
def Index():
    return render_template('ImageIndex.html')
@app.route('/ImageIndex')
def ImageIndex():
    return render_template('ImageIndex.html')
@app.route('/TextIndex')
def TextIndex():
    return render_template('TextIndex.html')

@app.route('/TextSearch',methods=['POST'])
def TextSearch():
    if request.method == 'POST':
        if not request.form['query']:
            return render_template('TextIndex.html')
        query=request.form['query']
        imgres, scores=SearchByText(query)
        if imgres:
            return render_template('TextSearch.html',imgres=imgres,scores=scores,query=query)
        return "Error indexing"
    return "Error uploading"


@app.route('/ImageSearch',methods=['POST'])
def ImageSearch():
    if request.method == 'POST':
        if request.files['file']:
            f = request.files['file']
            f.save(os.path.join(app.config['UPLOAD_FOLDER'], f.filename))
            caption, imgres, scores=SearchByCaption('C:/Users/Safaa/Desktop/ProjectIR/static/queries/'+f.filename)
            if caption:
                return render_template('ImageSearch.html',caption=caption,imgres=imgres,scores=scores,imgname='/static/queries/'+f.filename)
            return "Error captioning"
    return render_template('ImageIndex.html')

if __name__ == '__main__':
   app.run(debug = True)