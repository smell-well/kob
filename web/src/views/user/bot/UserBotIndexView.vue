<template>
    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card mt-3">
                    <div class="card-body d-flex justify-content-center">
                        <img :src="$store.state.user.photo" class="w-100">
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card mt-3">
                    <div class="card-header d-flex justify-content-between" style="align-items: center;">
                        <span class="fw-bold">我的Bot</span>
                        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#add_bot_btn">创建Bot</button>
                        <div class="modal fade modal-xl" id="add_bot_btn" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                <div class="modal-header">
                                    <h1 class="modal-title fs-5" id="exampleModalLabel">创建Bot</h1>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <form>
                                        <div class="mb-3">
                                            <label for="add_bot_name" class="form-label">Bot名称</label>
                                            <textarea v-model="botadd.title" class="form-control" id="add_bot_name" rows="1"></textarea>
                                        </div>
                                        <div class="mb-3">
                                            <label for="add_bot_description" class="form-label">Bot简介</label>
                                            <textarea v-model="botadd.description" class="form-control" id="add_bot_description" rows="3"></textarea>
                                        </div>
                                        <div class="mb-3">
                                            <label for="add_bot_code" class="form-label">Bot代码</label>
                                            <VAceEditor
                                                v-model:value="botadd.content"
                                                @init="editorInit"
                                                lang="c_cpp"
                                                theme="textmate"
                                                style="height: 300px" />
                                        </div>
                                    </form>
                                </div>
                                <div class="modal-footer">
                                    <div class="text-danger me-5">{{ botadd.error_message }}</div>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                    <button type="button" class="btn btn-primary" @click="add_bot">创建</button>
                                </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>名称</th>
                                    <th>创建时间</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id" style="vertical-align: middle;">
                                    <th class="fw-normal">{{ bot.title }}</th>
                                    <th class="fw-normal">{{ bot.createtime }}</th>
                                    <th>
                                        <button type="button" class="me-3 btn btn-secondary" data-bs-toggle="modal" :data-bs-target="'#update_bot_btn_'+bot.id">修改</button>
                                        <div class="modal fade modal-xl" :id="'update_bot_btn_' + bot.id" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                <div class="modal-header">
                                                    <h1 class="modal-title fs-5" id="exampleModalLabel">修改Bot</h1>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                </div>
                                                <div class="modal-body">
                                                    <form>
                                                        <div class="mb-3">
                                                            <label for="add_bot_name" class="form-label">Bot名称</label>
                                                            <textarea v-model="bot.title" class="form-control" id="add_bot_name" rows="1"></textarea>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="add_bot_description" class="form-label">Bot简介</label>
                                                            <textarea v-model="bot.description" class="form-control" id="add_bot_description" rows="3"></textarea>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="add_bot_code" class="form-label">Bot代码</label>
                                                            <VAceEditor
                                                                v-model:value="bot.content"
                                                                @init="editorInit"
                                                                lang="c_cpp"
                                                                theme="textmate"
                                                                style="height: 300px" />
                                                        </div>
                                                    </form>
                                                </div>
                                                <div class="modal-footer">
                                                    <div class="text-danger me-5">{{ bot.error_message }}</div>
                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                                    <button type="button" class="btn btn-primary" @click="update_bot(bot)">保存修改</button>
                                                </div>
                                                </div>
                                            </div>
                                        </div>
                                        <button type="button" class="btn btn-danger" @click="remove_bot(bot)">删除</button>
                                    </th>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                
            </div>
        </div>
    </div>

    <!-- Modal -->

</template>


<script>
import $ from 'jquery'
import { ref, reactive } from 'vue'
import { useStore } from 'vuex'
import { Modal } from 'bootstrap/dist/js/bootstrap'
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';

export default {
    components: {
        VAceEditor
    },
    setup() {
        ace.config.set(
            "basePath", 
            "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/");

        const store = useStore();
        let bots = ref([]);

        const botadd = reactive({
            title: "",
            description: "",
            content: "",
            error_message: "",
        })

        const refresh_bots = () => {
            $.ajax({
                url: "https://app1067.acapp.acwing.com.cn/api/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    bots.value = resp;
                },
            })
        }

        const add_bot = () => {
            botadd.error_message = "";
            $.ajax({
                url: "https://app1067.acapp.acwing.com.cn/api/user/bot/add/",
                type: "post",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                data: {
                    title: botadd.title,
                    description: botadd.description,
                    content: botadd.content,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        botadd.title = "";
                        botadd.description = "";
                        botadd.content = "";
                        Modal.getInstance("#add_bot_btn").hide();
                        refresh_bots();
                    } else {
                        botadd.error_message = resp.error_message;
                    }
                }
            })
        }

        const remove_bot = (bot) => {
            $.ajax({
                url: "https://app1067.acapp.acwing.com.cn/api/user/bot/remove/",
                type: "post",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                data: {
                    bot_id: bot.id,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        refresh_bots();
                    }
                }
            })
        }

        const update_bot = (bot) => {
            $.ajax({
                url: "https://app1067.acapp.acwing.com.cn/api/user/bot/update/",
                type: "post",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                data: {
                    bot_id: bot.id,
                    title: bot.title,
                    description: bot.description,
                    content: bot.content,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        Modal.getInstance("#update_bot_btn_" + bot.id).hide();
                        refresh_bots();
                    } else {
                        bot.error_message = resp.error_message;
                    }
                }
            })
        }

        refresh_bots();
        return {
            bots,
            botadd,
            add_bot,
            remove_bot,
            update_bot
        }
    }
}
</script>

<style scoped>
</style>