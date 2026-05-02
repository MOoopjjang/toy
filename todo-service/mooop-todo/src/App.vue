<script setup>
import { onMounted , getCurrentInstance } from 'vue';



const { proxy } = getCurrentInstance()




onMounted( ()=>{
});


</script>

<template>
  <div>
    <router-view name="header" />
    <router-view></router-view>
    <router-view name="footer" />
  </div>
</template>

<style>
.m-disabled{
  pointer-events: none; /* 클릭 막기 */
  background-color: #f5f5f5;
}

</style>
