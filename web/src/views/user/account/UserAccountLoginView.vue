<template>
    <ContentField v-if="!$store.state.user.pulling_info">
        <div class="row justify-content-center">
            <div class="col-3 ">
                <form @submit.prevent="login">
                    <div class="form-floating mb-3">
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                        <label style="display: inline-block; width: 100%;" for="username" class="form-label ttt">
                            <!-- <div class="ttt">
                                <div>用</div>
                                <div>户</div><div>名</div> -->
                            用户名
                            <!-- <div style="display: inline-block; width: 100%;"></div> -->
                            <!-- </div> -->
                        </label>
                    </div>
                    <div class="form-floating mb-3 text-center">
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
                        <label for="password" class="form-label">密码</label>
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
import { useStore } from 'vuex';
import { ref } from 'vue';
import router from '@/router/index';

export default {
    components: {
        ContentField,
    },

    setup() {
        const store = useStore();
        let username = ref("");
        let password = ref("");
        let error_message = ref("");

        const jwt_token = localStorage.getItem("jwt_token");
        if (jwt_token) {
            store.commit("updateToken", jwt_token);
            store.dispatch("getInfo", {
                // success(resp) {
                //     if (resp.error_message === "success") {
                //         router.push({name: "home"});
                //         console.log("PullingInfo " + store.state.user.pulling_info);
                //         store.commit("updatePullingInfo", false);
                //     }
                // },
                success() {
                    router.push({name: "home"});
                    store.commit("updatePullingInfo", false);
                },
                error() {
                    store.commit("updatePullingInfo", false);
                }
            });
        } else {
            store.commit("updatePullingInfo", false);
        }

        const login = () => {
            error_message.value = "";
            store.dispatch("login", {
                username: username.value,
                password: password.value,
                success() {
                    store.dispatch("getInfo", {
                        success() {
                            router.push({name: 'home'});
                            console.log(store.state.user);
                        }
                    })
                },
                error() {
                    error_message.value = "用户名或密码错误";
                    // console.log(resp);
                }
            })
        };

        return {
            username,
            password,
            error_message,
            login,
        }
    }
}
</script>

<style scoped>
</style>