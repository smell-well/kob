<template>
    <div class="matchground">
        <div class="row">
            <div class="col-4">
                <div class="user_photo">
                    <img :src="$store.state.user.photo"/>
                </div>
                <div class="fw-bold fs-3 pt-2 text-white user_name">
                    {{ $store.state.user.username }}
                </div>
            </div>
            <div class="col-4 user-select-bot">
                <div class="user-select-bot">
                    <select v-model="select_bot" class="form-select " aria-label="Default select example">
                        <option value="-1" selected>亲自上阵</option>
                        <option v-for="bot in bots" :key="bot.id" :value="bot.id">
                            {{ bot.title }}
                        </option>
                    </select>
                </div>
            </div>

            <div class="col-4">
                <div class="user_photo" >
                    <img :src="$store.state.pk.opponent_photo"/>
                </div>
                <div class="fw-bold fs-3 pt-2 text-white user_name">
                    {{ $store.state.pk.opponent_username }}
                </div>
            </div>
            <div class="d-flex col-12 justify-content-center" style="padding-top: 15vh;">
                <button type="button" class="btn btn-success btn-lg" @click="click_match_btn">{{ match_btn_info }}</button>
            </div>
        </div>
    </div>
</template>

<script>
import { useStore } from 'vuex'
import { ref } from 'vue'
import $ from 'jquery'


export default {
    setup() {
        let match_btn_info = ref('开始匹配');
        const store = useStore();
        const bots = ref([]);
        let select_bot = ref('-1');

        const click_match_btn = () => {
            if (match_btn_info.value === "开始匹配") {
                match_btn_info.value = "取消";
                // console.log(select_bot.value)
                store.state.pk.socket.send(JSON.stringify({
                    event: "start_matching",
                    bot_id: select_bot.value
                }));
            } else {
                match_btn_info.value = "开始匹配";
                store.state.pk.socket.send(JSON.stringify({
                    event: "stop_matching",
                }));
            }
        }

        const refresh_bots = () => {
            // console.log(store.state.user.token)
            $.ajax({
                url: "https://app1067.acapp.acwing.com.cn/api/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    bots.value = resp;
                },
                error(resp) {
                    console.log(resp)
                }
            })
        }
        refresh_bots();

        return {
            match_btn_info,
            click_match_btn,
            bots,
            select_bot
        }
    }

}
</script>

<style scoped>
div.matchground {
    width: 60vw;
    height: 70vh;
    margin: 40px auto 20px auto;
    background-color: rgba(50, 50, 50, 0.5);
    border-radius: 5%;
}

div.user_photo {
    text-align: center;
    padding-top: 15vh;
}

div.user_photo > img {
    border-radius: 50%;
    width: 20vh;
}

div.user_name {
    text-align: center;
}

div.user-select-bot {
    padding-top: 13vh;
}

div.user-select-bot > select {
    width: 70%;
    margin: 0 auto;
}


</style>