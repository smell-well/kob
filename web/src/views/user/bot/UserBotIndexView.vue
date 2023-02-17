<template>
    <ContentField>我的bot</ContentField>
</template>


<script>
import ContentField from '@/components/ContentField.vue'
import $ from 'jquery'
import { useStore } from 'vuex'

export default {
    components: {
        ContentField,
    },
    setup() {
        const store = useStore();

        // $.ajax({
        //     url: "http://localhost:3000/user/bot/add/",
        //     type: "POST",
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token,
        //     },
        //     data: {
        //         title: "bot名称",
        //         description: "bot描述",
        //         content: "bot代码",
        //     },
        //     success(resp) {
        //         console.log(resp);
        //     },
        //     error(resp) {
        //         console.log(resp);
        //     }
        // })

        // $.ajax({
        //     url: "http://localhost:3000/user/bot/remove/",
        //     type: "POST",
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token,
        //     },
        //     data: {
        //         bot_id: "1",
        //     },
        //     success(resp) {
        //         console.log(resp);
        //     },
        //     error(resp) {
        //         console.log(resp);
        //     }
        // })

        // $.ajax({
        //     url: "http://localhost:3000/user/bot/update/",
        //     type: "POST",
        //     headers: {
        //         Authorization: "Bearer " + store.state.user.token,
        //     },
        //     data: {
        //         bot_id: "2",
        //         title: "bot名称new",
        //         description: "bot描述new",
        //         content: "bot代码new",
        //     },
        //     success(resp) {
        //         console.log(resp);
        //     },
        //     error(resp) {
        //         console.log(resp);
        //     }
        // })

        $.ajax({
            url: "http://localhost:3000/user/bot/getlist/",
            type: "get",
            headers: {
                Authorization: "Bearer " + store.state.user.token,
            },
            data: {
            },
            success(resp) {
                console.log(resp);
            },
            error(resp) {
                console.log(resp);
            }
        })
    }
}
</script>

<style scoped>
</style>