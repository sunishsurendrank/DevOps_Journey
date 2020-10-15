#Author of this Document : Sunish Surendran Kannembath
#Developer : Sunish Surendran Kannembath
#Reach Sunish in LinkedIn : https://www.linkedin.com/in/sunishsurendrank/
#Reach Sunish in Twitter : @sunishsurendran


from abc import ABC, abstractmethod
from flask import Flask, jsonify
import time , concurrent.futures

app = Flask(__name__)

#Declaring Abstract Class
class Execute(ABC):

    def __init__(self):
        super().__init__()
        
    @abstractmethod
    def ExecuteCode(self):
        pass

class ExecuteConcurrently(Execute):

    def __init__(self,count):
        self.count = count

   
    def ExecuteCode(self):

        start_time = time.time()

        with concurrent.futures.ProcessPoolExecutor() as executor:
            for _ in range(self.count):
                executor.submit(time.sleep, 1)

        end_time = time.time()
        elspase_time = end_time-start_time
        return jsonify({"concurrent_time": elspase_time})



    #Magic method
    def __repr__(self):
        return f"{self.count}"

class ExecuteSequentially(Execute):

    def __init__(self,count):
        self.count = count
    
   
    def ExecuteCode(self):

        start_time = time.time()
        
        for _ in range(self.count):
            time.sleep(1)

        end_time = time.time()
        elspase_time = end_time-start_time
        return jsonify({"serial_time": elspase_time})
    
    #Magic method
    def __repr__(self):
        return f"{self.count}"

@app.route('/sequential')
def sequential():
    ObjectExecuteSequentially  = ExecuteSequentially(4)
    ExecuteSequentiallyResult = ObjectExecuteSequentially.ExecuteCode()
    return ExecuteSequentiallyResult

@app.route("/concurrent")
def concurrently():
    ObjectExecuteConcurrently =  ExecuteConcurrently(4)
    ExecuteConcurrentlyResult =  ObjectExecuteConcurrently.ExecuteCode()
    return ExecuteConcurrentlyResult
    
if __name__ == "__main__":

    app.run(host='0.0.0.0',debug=True, port=5000)





