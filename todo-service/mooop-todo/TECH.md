# 주요기술  

## event bus  
```
- "mitt": "^3.0.1" 설치 
  
- 선언
  main.js
  ..
  import mitt from 'mitt'
  ..
  /** Event Bus */
  const emitter = mitt()
  app.config.globalProperties.$emitter = emitter

- 수신
  onMounted(()=>{
    /** Event 수신  */
    proxy.$emitter.on('delete' , deleteHandler)
  })


  const deleteHandler = async (value)=>{
    // 기능 동작...
  }

- 발신
    proxy.$emitter.emit('delete' , '')
```  

## pinia ( 상태값 관리 )  
```
- "pinia": "^3.0.4" 설치

- 선언
  main.js

  import { createPinia } from 'pinia'
  ...
  /** state manager */
  app.use(createPinia())
   ...



********************
  sample.js ( define )
  import { defineStore } from 'pinia'


  export const sampleStore = defineStore('sample-store-id',{
      state: ()=>({
          data:'',
          data2:'',
          data3: 0
      }),
      actions:{
          getData(){
            return this.data
          },
          setData(_v){
            return this.data = _v
          }
          ....
      }
  })


********************

    sample.vue

    import { sampleStore } from '../store/sample'

    const sampleInfoStore = sampleStore()

    sampleInfoStore.getData()



  
```