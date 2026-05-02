import { createApp } from 'vue'

import mitt from 'mitt'
import './style.css'
import App from './App.vue'

import { createPinia } from 'pinia'
import  router  from './router/index.js'
import { AppCode } from './code/AppCodes.js'
import  api  from './service/api.js'


const app = createApp(App)
app.config.globalProperties.$api = api

/** Event Bus */
const emitter = mitt()
app.config.globalProperties.$emitter = emitter

/** state manager */
app.use(createPinia())

app.use(router)

app.mount('#app')
