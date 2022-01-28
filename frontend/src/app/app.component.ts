import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";

export interface Currencies {
  currencies: string[]
}

export interface ConversionResult {
  date: string
  amount: string
  status: string
}

export interface History {
  dateTime: string
  curFrom: string
  curTo: string
  amount: string
  exRate: string
  sumConverted: string
}

export interface Stat {
  curFrom: string
  curTo: string
  amountConverted: string
  averageExRate: string
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{

  currencies: string[] = []

  currFrom: string = ''
  sumToConvert: number = 0
  currTo: string = ''
  timeout: any
  sumAfterConversion: string = '0';

  date: string
  amount: string

  displayHistory: boolean = false
  history: History[] = []
  displayStat: boolean = false
  stat: Stat[] = []
  showBottom: boolean = false


  constructor(private http: HttpClient) {
  }


  ngOnInit(): void {
    this.http.get<Currencies>("http://localhost:8080").subscribe(response => {
      this.currencies = response.currencies
    })
  }

  sourceCurrencyHandler(event:any) {
    this.currFrom = event.target.value
    if(this.currTo != '' && this.sumToConvert > 0){
      this.convert()
    }
  }

  targetCurrencyHandler(event:any) {
    this.currTo = event.target.value
    if(this.currFrom != '' && this.sumToConvert > 0){
      this.convert()
    }
  }

  sumInputHandler(event:any) {
    this.timeout = setTimeout(() => {
      if(event.target.value != '' && event.target.value != 0){
        this.sumToConvert = event.target.value
        if(this.currFrom != '' && this.currTo != ''){
          this.convert()
        }
      } else {
        this.resetHandler()
      }
    }, 2000)
  }

  sumKeypressHandler() {
    clearTimeout(this.timeout)
  }

  resetHandler() {
    this.sumToConvert = 0
    this.amount = ''
  }

  convert(){
    let params = new HttpParams()
      .set("curfrom", this.currFrom)
      .set("curto", this.currTo)
      .set("amount", this.sumToConvert.toString());
    this.http.get<ConversionResult>("http://localhost:8080/convert",
      {params: params})
      .subscribe(response => {
        this.date = response.date
        this.amount = response.amount
    })
  }

  showHistory() {
    this.displayHistory = !this.displayHistory
    if(this.displayStat){
      this.displayStat = !this.displayStat
    }
    this.http.get<History[]>("http://localhost:8080/history").subscribe(response => {
      this.history = response
    })
  }

  showStat() {
    this.displayStat = !this.displayStat
    if(this.displayHistory){
      this.displayHistory = !this.displayHistory
    }
    this.http.get<Stat[]>("http://localhost:8080/stat").subscribe(response => {
      this.stat = response
    })
  }
}
