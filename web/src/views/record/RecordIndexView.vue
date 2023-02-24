<template>
    <ContentField>
        <table class="table table-striped table-hover records">
            <thead>
                <tr>
                    <th>A</th>
                    <th>B</th>
                    <th>对战结果</th>
                    <th>对战时间</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="record in records" :key="record.record.id" style="vertical-align: middle;">
                    <th>
                        <div>
                            <img class="user-photo" :src="record.a_photo"/>
                            &nbsp;
                            <span>{{ record.a_username }}</span>
                        </div>
                    </th>
                    <th>
                        <div>
                            <img class="user-photo" :src="record.b_photo"/>
                            &nbsp;
                            <span>{{ record.b_username }}</span>
                        </div>
                    </th>
                    <th>
                        <div v-if="record.record.loser === 'A' && parseInt($store.state.user.id) !== parseInt(record.record.aid)">
                            <span class="text-success">胜利</span>
                        </div>
                        <div v-else-if="record.record.loser === 'B' && parseInt($store.state.user.id) !== parseInt(record.record.bid)">
                            <span class="text-success">胜利</span>
                        </div>
                        <div v-else-if="record.record.loser === 'all'">
                            <span class="text-secondary">平局</span>
                        </div>
                        <div v-else>
                            <span class="text-danger">失败</span>
                        </div>
                    </th>
                    <th>
                        {{ record.record.createtime }}
                    </th>
                    <th>
                        <button type="button" class="btn btn-secondary" @click="open_record_content(record.record.id)">查看录像</button>
                    </th>
                </tr>
            </tbody>
        </table>
        <nav aria-label="Page navigation example">
            <ul class="pagination float-end">
                <li class="page-item" @click="click_page(-2)">
                    <a class="page-link" href="#" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
                <li :class="'page-item ' + page.is_active"  v-for="page in pages" :key="page.number" @click="click_page(page.number)">
                    <a class="page-link" href="#">{{ page.number }}</a>
                </li>
                <li class="page-item" @click="click_page(-1)">
                    <a class="page-link" href="#" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </ul>
        </nav>
    </ContentField>
</template>


<script>
import ContentField from '@/components/ContentField.vue';
import $ from 'jquery';
import { useStore } from 'vuex';
import { ref } from 'vue';
import router from '@/router/index';

export default {
    components: {
        ContentField,
    },
    setup() {
        const store = useStore();
        let current_page = 1;
        let records = ref([]);
        let records_count = 0;
        let pages = ref([]);

        const update_pages = () => {
            let max_pages = parseInt(Math.ceil(records_count / 10));
            let new_pages = [];
            for (let i = current_page - 2; i <= current_page + 2; i++) {
                if (i >= 1 && i <= max_pages) {
                    new_pages.push({
                        number: i,
                        is_active: current_page === i ? "active" : ""
                    })
                }
            }
            pages.value = new_pages;
        }

        const click_page = number => {
            if (number === -2) number = current_page - 1;
            if (number === -1) number = current_page + 1;
            let max_pages = parseInt(Math.ceil(records_count / 10));
            
            
            if (number >= 1 && number <= max_pages) {
                console.log(number)
                pull_page(number);
            }

            
        }

        const pull_page = page => {
            current_page = page
            $.ajax({
                url: "https://app1067.acapp.acwing.com.cn/api/record/getlist/",
                type: "get",
                data: {
                    page: current_page,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    records.value = resp.records;
                    records_count = resp.record_count;
                    update_pages();
                    console.log(resp)
                    console.log("id " + store.state.user.id)
                },
                error(resp) {
                    console.log(resp)
                }
            })
        }

        const stringTo2D = map => {
            let g = []
            for (let i = 0; i < 13; i++) {
                let r = []
                for (let j = 0; j < 14; j++) {
                    if (map[i * 14 + j] === '1') {
                        r.push(1);
                    } else r.push(0);
                }
                g.push(r);
            }
            return g
        }

        const open_record_content = (record_id) => {
            for (let record of records.value) {
                if (record.record.id === record_id) {
                    // const g = stringTo2D(record.record.map);

                    store.commit("updateGame", {
                        map: stringTo2D(record.record.map),
                        a_id: record.record.aid,
                        a_sx: record.record.asx,
                        a_sy: record.record.asy,
                        b_id: record.record.bid,
                        b_sx: record.record.bsx,
                        b_sy: record.record.bsy,

                    });

                    store.commit("updateIsRecord", true);
                    store.commit("updateAsteps", record.record.asteps);
                    store.commit("updateBsteps", record.record.bsteps);
                    store.commit("updateRecordLoser", record.record.loser);
                    router.push({
                        name: 'record_content',
                        params: {
                            record_id
                        }
                    });
                    break;
                }
            }
        }

        pull_page(current_page);
        return {
            records,
            records_count,
            open_record_content,
            pages,
            click_page
        }
    }
}
</script>

<style scoped>
img.user-photo {
    border-radius: 50%;
    width: 4vh;
}

table.records {
    text-align: center;
}
</style>