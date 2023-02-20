<template>
    <div class="result-board">
        <div class="result-board-text fw-bold text-white" v-if="$store.state.pk.loser === 'all'">
            draw
        </div>
        <div class="result-board-text fw-bold text-white" v-else-if="$store.state.pk.loser === 'A' && $store.state.pk.a_id === parseInt($store.state.user.id)">
            lose
        </div>
        <div class="result-board-text fw-bold text-white" v-else-if="$store.state.pk.loser === 'B' && $store.state.pk.b_id === parseInt($store.state.user.id)">
            lose
        </div>
        <div class="result-board-text fw-bold text-white" v-else>
            win
        </div>
        <div class="result-board-btn">
            <button type="button" class="btn btn-success" @click="restart">
                再来一局!
            </button>
        </div>
    </div>
</template>


<script>
import { useStore } from 'vuex'

export default {
    setup() {
        const store = useStore();
        
        const restart = () => {
            store.commit("updateStatus", "matching");
            store.commit("updateLoser", "none");
            store.commit("updateOpponent", {
                username: "对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            })
        }

        return {
            restart,
        }
    }
}

</script>

<style scoped>
div.result-board {
    height: 30vh;
    width: 30vw;
    background-color: rgba(50, 50, 50, 0.5);
    position: absolute;
    top: 30vh;
    left: 35vw;
    border-radius: 5%;
}

div.result-board-text {
    font-size:xx-large;
    font-style: italic;
    padding-top: 7vh;
    text-align: center;
    
}

div.result-board-btn {
    text-align: center;
    padding-top: 5vh;
}
</style>