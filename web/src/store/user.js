import $ from 'jquery'

export default {
    state: {
        id: "",
        username: "",
        photo: "",
        token: "",
        is_login: false,
        pulling_info: true,
    },
    getters: {
    },
    mutations: {
        updateUser(state, user) {
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = user.is_login;
        },

        updateToken(state, token) {
            state.token = token;
        },

        updatePullingInfo(state, flag) {
            state.pulling_info = flag;
        },

        logout(state) {
            state.id = "";
            state.username = "";
            state.photo = "";
            state.token = "";
            state.is_login = false;
        }
    },
    actions: {
        login(context, data) {
            $.ajax({
                url: "https://app1067.acapp.acwing.com.cn/api/user/account/token/",
                type: "post",
                data: {
                    username: data.username,
                    password: data.password,
                },
                success(resp) {
                    console.log(resp.error_message);
                    if (resp.error_message === "success") {
                        localStorage.setItem("jwt_token", resp.token);
                        context.commit("updateToken", resp.token);
                        context.commit("updateUser", {
                            ...resp,
                            is_login: true
                        });
                        data.success();
                    } else {
                        data.error(resp);
                    }
                    console.log(resp)
                },
                error(resp) {
                    data.error(resp);
                }
            });
        },
        getInfo(context, data) {
            $.ajax({
                url: "https://app1067.acapp.acwing.com.cn/api/user/account/info/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + context.state.token,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        context.commit("updateUser", {
                            ...resp,
                            is_login: true
                        });
                        data.success(resp);
                    } else {
                        data.error(resp);
                    }
                    console.log(resp)
                },
                error(resp) {
                    if (resp.status == 401) {
                        const e = JSON.parse(resp.responseText)
                        console.log(e)
                        localStorage.removeItem("jwt_token");
                        context.commit("updateToken", null);
                        data.error()
                    }                    
                }
            });
        },
        logout(context) {
            context.commit("logout");
        }
    },
    modules: {
    }
}