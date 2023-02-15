<template>
  <NavBar />
  <router-view/>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
import $ from 'jquery'

export default {
  components: {
    NavBar,
  },

  setup() {
    $.ajax({
      url: "http://localhost:3000/user/account/token/",
      type: "post",
      data: {
        username: 'u2',
        password: 'p2',
      },
      success(resp) {
        console.log(resp)
      },
      error(resp) {
        console.log(resp)
      }
    });

    $.ajax({
      url: "http://localhost:3000/user/account/info/",
      type: "get",
      headers: {
        Authorization: "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJlY2I0YjI2NjI3MTQ0YTFkYjQyNmE1YTRhNmViOTg2ZiIsInN1YiI6IjIiLCJpc3MiOiJzZyIsImlhdCI6MTY3NjQ0NjA1NiwiZXhwIjoxNjc3NjU1NjU2fQ.-me8nee4_mWR_j5-Rj9OFSM2b9JJuCuKvkG_ueplpUo",
      },
      success(resp) {
        console.log(resp)
      },
      error(resp) {
        console.log(resp)
      }
    });

    $.ajax({
      url: "http://localhost:3000/user/account/register/",
      type: "post",
      data: {
        username: "yxc",
        password: "123",
        confirmedPasswrod: "123",
      },
      success(resp) {
        console.log(resp)
      },
      error(resp) {
        console.log(resp)
      }
    })
  }
}

</script>


<style>
body {
  background-image: url("@/assets/image/background.png");
  background-size: cover;
}
</style>
