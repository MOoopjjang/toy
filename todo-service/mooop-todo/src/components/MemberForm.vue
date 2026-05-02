<script setup>
import { reactive , ref , onMounted , onUnmounted , getCurrentInstance , defineEmits , defineProps } from 'vue'
import { useRouter } from 'vue-router'
import { ApiResultCode , ApiUrlCode } from '../code/ApiCode'

// import { userAuthStore } from '../store/auth'
import {memberStore} from '../store/member'

const { proxy } = getCurrentInstance()
const router = useRouter()
const emits = defineEmits(['close'])

// const authInfoStore = userAuthStore()
const memberInfoStore = memberStore()



const props = defineProps({
  mode: String
})

const memberForm = reactive({
  "email":"",
  "userid":"",
  "password":"",
  "address":"",
  "profileImage":""
})

let file = null

const closeModal = () => {
  emits('close')
}

/** TODO : 중복코드 -> 공통화 필요!!! */
const handleApiError = (e)=>{
  if(!e.response){
    alert('네트워크 error')
    return
  }

  const status = e.response.status
  switch(status){
    case 400:
      alert('400 error')
      break
    case 404:
      alert('400 error')
      break
    default:
      console.error('unhandle error')
  }
}

const onSubmit = () => {

  const formData = new FormData()
  formData.append("memberRegisterRequest" , new Blob([JSON.stringify(memberForm)] , {type: 'application/json'}))
  if(file){
    formData.append("photo" , file)
  }

    proxy.$api.post( ApiUrlCode.MEMBER_REG , formData , {
      headers:{
        'Content-Type': 'multipart/form-data'
      }
    }).then((apiResponse)=>{
      if(apiResponse.data.result === ApiResultCode.FAIL){
        alert(apiResponse.data.message)
      }else{
        alert('가입되었습니다.')
        closeModal()
        router.push({name:'login'})
      }
    }).catch(handleApiError)
}

const onChange = ()=>{

  let memberUpdateRequest = {
    "email":memberForm.email
    ,"password":memberForm.password
    ,"address":memberForm.address
  }


  const formData = new FormData()
  formData.append("memberUpdateRequest" , new Blob([JSON.stringify(memberUpdateRequest)] , {type: 'application/json'}))
  if(file){
    formData.append("photo" , file)
  }
  
  proxy.$api.post(ApiUrlCode.MEMBER_MODIFY , formData , {
     headers:{
      'Content-Type': 'multipart/form-data'
      }
  }).then((apiResponse)=>{
    if(apiResponse.data.result === ApiResultCode.FAIL){
        alert(apiResponse.data.message)
      }else{
        alert('변경 되었습니다.')

        proxy.$emitter.emit('headerUpdate' , 'update')
        closeModal()
        router.push({name:'main'})
      }

  }).catch(handleApiError)

}


const onFileChange = (_file) => {
  file = _file.target.files[0]
}


onMounted(()=>{

  if(props.mode === 'MODIFY'){
     memberForm.email =  memberInfoStore.email
     memberForm.userid = memberInfoStore.userid
     memberForm.password = memberInfoStore.password
     memberForm.address = memberInfoStore.address
  }
  
})


</script>
<template>
   <!-- Modal -->
  <div class="modal-overlay" @click.self="closeModal">

    <div class="modal-container">

      <!-- 닫기 버튼 -->
      <div class="modal-header">
        <span v-if="mode === 'REG'">회원 등록</span>
        <span v-else>회원정보 변경</span>
        <button class="close-btn" @click="closeModal">×</button>
      </div>

      <!-- 내용 -->
      <div class="modal-body">

        <!-- 이메일 -->
        <div class="form-group">
          <label>이메일</label>
          <input v-model="memberForm.email" type="email" />
        </div>

        <!-- 아이디 -->
        <div class="form-group">
          <label>사용자 아이디</label>
          <input v-model="memberForm.userid" type="text" />
        </div>

        <!-- 암호 -->
        <div class="form-group">
          <label>암호</label>
          <input v-model="memberForm.password" type="password" />
        </div>

        <!-- 주소 -->
        <div class="form-group">
          <label>주소</label>
          <input v-model="memberForm.address" type="text" />
        </div>

        <!-- 파일 -->
        <div class="form-group">
          <label>사진 업로드</label>

          <div class="file-upload">
            <input type="file" id="fileInput" name="file" @change="onFileChange" />
            <label for="fileInput" class="upload-btn">사진 선택</label>
            <span class="file-name">{{ fileName }}</span>
          </div>
        </div>

      </div>

      <!-- footer -->
      <div class="modal-footer">
        <button class="btn-cancel" @click="closeModal">취소</button>
        <button v-if="mode === 'REG'" class="btn-submit" @click="onSubmit">등록</button>
        <button v-else  class="btn-submit" @click="onChange">변경</button>
      </div>

    </div>

  </div>

</template>
<style scoped>
/* overlay */
.modal-overlay {

  position: fixed;

  top: 0;
  left: 0;

  width: 100%;
  height: 100%;

  background: rgba(0,0,0,0.4);

  display: flex;
  align-items: center;
  justify-content: center;

  z-index: 999;
}

/* container */
.modal-container {

  width: 100%;
  max-width: 520px;

  background: #ffffff;

  border-radius: 16px;

  box-shadow: 0 10px 30px rgba(0,0,0,0.15);

  display: flex;
  flex-direction: column;

  animation: fadeIn 0.2s ease;
}

/* header */
.modal-header {

  display: flex;
  justify-content: space-between;
  align-items: center;

  padding: 16px 20px;

  font-weight: 700;
  font-size: 18px;

  border-bottom: 1px solid #eeeeee;
}

/* 닫기 버튼 */
.close-btn {

  border: none;
  background: none;

  font-size: 22px;

  cursor: pointer;
}

/* body */
.modal-body {

  padding: 20px;

  display: flex;
  flex-direction: column;

  gap: 14px;
}

/* footer */
.modal-footer {

  display: flex;
  justify-content: flex-end;

  gap: 10px;

  padding: 16px 20px;

  border-top: 1px solid #eeeeee;
}

/* form */
.form-group {
  display: flex;
  flex-direction: column;
  text-align: left;
}

.form-group label {
  font-size: 13px;
  margin-bottom: 4px;
  color: #616161;
}

.form-group input {

  height: 38px;

  border-radius: 8px;

  border: 1px solid #e0e0e0;

  padding: 0 10px;
}

/* file */
.file-upload {
  display: flex;
  align-items: center;
  gap: 10px;
}

.file-upload input {
  display: none;
}

.upload-btn {

  height: 34px;

  padding: 0 10px;

  border-radius: 8px;

  background: #e3f2fd;

  color: #0d47a1;

  border: 1px solid #bbdefb;

  cursor: pointer;
}

/* 버튼 */
.btn-cancel {

  height: 36px;

  padding: 0 14px;

  border-radius: 8px;

  border: none;

  background: #e0e0e0;

  cursor: pointer;
}

.btn-submit {

  height: 36px;

  padding: 0 16px;

  border-radius: 8px;

  border: none;

  background: linear-gradient(135deg, #90caf9, #64b5f6);

  color: #0d47a1;

  font-weight: 600;

  cursor: pointer;
}

/* 애니메이션 */
@keyframes fadeIn {
  from {
    transform: translateY(10px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}
</style>
