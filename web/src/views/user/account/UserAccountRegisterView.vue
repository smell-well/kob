<template>
    <ContentField>
        <div class="row justify-content-center">
            <div class="col-3 ">
                <form @submit.prevent="register">
                    <div class="form-floating mb-3">
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                        <label style="display: inline-block; width: 100%;" for="username" class="form-label ttt">
                            用户名
                        </label>
                    </div>
                    <div class="form-floating mb-3 text-center">
                        <input v-model="password" type="password" class="form-control" id="password"
                            placeholder="请输入密码">
                        <label for="password" class="form-label">密码</label>
                    </div>
                    <div class="form-floating mb-3 text-center">
                        <input v-model="confirmedPassword" type="password" class="form-control" id="confirmedPassword"
                            placeholder="请再次输入密码">
                        <label for="password" class="form-label">确认密码</label>
                    </div>
                    <div class="text-center text-danger mb-2">{{ error_message }}</div>
                    <div class="d-grid">
                        <button type="submit" class="btn btn-primary">提交</button>
                    </div>
                </form>
            </div>
        </div>
    </ContentField>
</template>


<script>
import ContentField from '@/components/ContentField.vue'
import { ref } from 'vue'
import $ from 'jquery'
import router from '@/router/index'

export default {
    components: {
        ContentField,
    },
    setup() {
        let username = ref("");
        let password = ref("");
        let confirmedPassword = ref("");
        let error_message = ref("");

        const register = () => {
            error_message.value = "";
            $.ajax({
                url: "http://localhost:3000/user/account/register/",
                type: "post",
                data: {
                    username: username.value,
                    password: password.value,
                    confirmedPasswrod: confirmedPassword.value,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        router.push({name: "user_account_login"});
                    } else {
                        error_message.value = resp.error_message;
                    }
                    console.log(resp)
                },
                error(resp) {
                    console.log(resp)
                }
            })
        }

        return {
            username,
            password,
            confirmedPassword,
            error_message,
            register,
        }
    }
}
</script>

<style scoped>

</style>