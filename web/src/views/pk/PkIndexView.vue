<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'"/>
    <MatchGround v-if="$store.state.pk.status === 'matching'"/>
</template>


<script>
import PlayGround from '@/components/PlayGround.vue'
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';
import MatchGround from '@/components/MatchGround.vue'

export default {
    components: {
        PlayGround,
        MatchGround
    },
    setup() {
        const store = useStore();
        const socketUrl = `ws://localhost:3000/websocket/${store.state.user.token}`;

        let socket = null;
        console.log(socketUrl);
        onMounted(() => {
            store.commit("updateOpponent", {
                username: "对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            })

            socket = new WebSocket(socketUrl);

            socket.onopen = () => {
                console.log("connected!");
                store.commit("updateSocket", socket);
            }

            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                if (data.event === "start_matching") {
                    store.commit("updateOpponent", {
                        username: data.opponent_username,
                        photo: data.opponent_photo
                    });
                    console.log(data.gamemap);
                    store.commit("updateGamemap", data.gamemap);

                    setTimeout(() => {
                        store.commit("updateStatus", "playing");
                    }, 2000);

                }
            }

            socket.onclose = () => {
                console.log("disconnected!");
            }

        });

        onUnmounted(() => {
            socket.close();
            store.commit("updateStatus", "matching");
        });
    }
}
</script>

<style scoped>
</style>