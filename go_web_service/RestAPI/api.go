package main

import (
	"fmt"
	"time"
	"net/http"
	"encoding/json"
)
type JSON_Response struct  {
	TimeToExecute float64	 `json:"TimeToExecute"`

}
type Execute interface {

	ExecuteCode() float64 
}

type ExecuteSequentially struct {

	Count int

}

func (e ExecuteSequentially) ExecuteCode() float64{

	start_time := time.Now()

	for i := 0; i < e.Count ; i++ {

		time.Sleep(time.Second)
		
	}
	elapsed_time :=  time.Since(start_time)

	return elapsed_time.Seconds()
}

type ExecuteConcurrently struct {

	Count int

}

func (e ExecuteConcurrently) ExecuteCode() float64{

	start_time := time.Now()

	channel := make(chan int)

	for i := 0 ; i < e.Count ; i++ {
	  
		go func(c chan int) {
			time.Sleep(time.Second)
			c <- 1
		}(channel)

	}

	for i := 0 ; i < e.Count ; i++ {
		<-channel
	}
	
	elapsed_time :=  time.Since(start_time)

	return elapsed_time.Seconds()


	
}

func Polymorphic(executeref Execute) float64{

	return executeref.ExecuteCode()

}

func Sequential(rw http.ResponseWriter, r *http.Request){
	//creating a object for struct
	ObjectExecuteSequentially := ExecuteSequentially{Count : 4}
	ExecuteSequentiallyResult := Polymorphic(ObjectExecuteSequentially)

	rw.Header().Set("Content-Type", "application/json")

	response := JSON_Response{
		TimeToExecute: ExecuteSequentiallyResult}

	json.NewEncoder(rw).Encode(response)
	

}

func Concurrently(rw http.ResponseWriter, r *http.Request){
	ObjectExecuteConcurrently := ExecuteConcurrently{Count : 4}
	ExecuteConcurrentlyResult := Polymorphic(ObjectExecuteConcurrently)

	rw.Header().Set("Content-Type", "application/json")
	
	response := JSON_Response{
		TimeToExecute: ExecuteConcurrentlyResult}

	json.NewEncoder(rw).Encode(response)
}

func main() {


	http.HandleFunc("/sequential",Sequential)
	http.HandleFunc("/concurrent",Concurrently)

	fmt.Printf("Starting the server at Port 9090 \n")
	err := http.ListenAndServe(":9090", nil)
	fmt.Println(err)


}