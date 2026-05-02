# Tech 노트


## 시간관련 처리  

- FO 와 BE간에 날짜관련 Data 처리  
ㄴ BE : LocalDateTime to String --> FO: String  
```
object DateUtil {

    fun convertLDTToString(ldt:LocalDateTime , fmt:String? = null):String{
        val tmp = fmt?:"yyyy-MM-dd HH:mm:ss"
        val formatter = DateTimeFormatter.ofPattern(tmp)
        return ldt.format(formatter)
    }
}
```


##  VO관련 코드 마무리  

- Email , Password , Image , Progress , Priority 내부 validation 코드 추가 필요  
ㄴ init { ... }에 로직 추가  
```
ex)
data class Priority(val priorityValue:Int){
    init{
        if((priorityValue < -1) || (priorityValue > 99)){
            throw Exception("우선순위 범위는 -1 ~ 99 까지 입니다.")
        }
    }
}
```

