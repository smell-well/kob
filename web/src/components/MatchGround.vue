<template>
    <div class="matchground">
        <div class="row">
            <div class="col-6">
                <div class="user_photo">
                    <img :src="$store.state.user.photo"/>
                </div>
                <div class="fw-bold fs-3 pt-2 text-white user_name">
                    {{ $store.state.user.username }}
                </div>
            </div>
            <div class="col-6">
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


export default {
    setup() {
        let match_btn_info = ref('开始匹配');
        const store = useStore();

        const click_match_btn = () => {
            if (match_btn_info.value === "开始匹配") {
                match_btn_info.value = "取消";
                store.state.pk.socket.send(JSON.stringify({
                    event: "start_matching",
                }));
            } else {
                match_btn_info.value = "开始匹配";
                store.state.pk.socket.send(JSON.stringify({
                    event: "stop_matching",
                }));
            }
        }

        return {
            match_btn_info,
            click_match_btn
        }
    }

}
</script>

<style scoped>
div.matchground {
    width: 60vw;
    height: 70vh;
    margin: 40px auto;
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
</style>