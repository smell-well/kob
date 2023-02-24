<template>
    <ContentField>
        <table class="table table-striped table-hover records">
            <thead>
                <tr>
                    <th>玩家</th>
                    <th>天梯分</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="user in users" :key="user.id" style="vertical-align: middle;">
                    <td>
                        <img class="user-photo" :src="user.photo"/>
                            &nbsp;
                        <span>{{ user.username }}</span>
                    </td>
                    <th>
                        <span>{{ user.rating }}</span>
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

export default {
    components: {
        ContentField,
    },
    setup() {
        const store = useStore();
        let current_page = 1;
        let users = ref([]);
        let users_count = 0;
        let pages = ref([]);

        const update_pages = () => {
            let max_pages = parseInt(Math.ceil(users_count / 10));
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
            let max_pages = parseInt(Math.ceil(users_count / 10));
            
            
            if (number >= 1 && number <= max_pages) {
                console.log(number)
                pull_page(number);
            }

            
        }

        const pull_page = page => {
            current_page = page
            $.ajax({
                url: "https://app1067.acapp.acwing.com.cn/api/ranklist/getlist/",
                type: "get",
                data: {
                    page: current_page,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    users.value = resp.users;
                    users_count = resp.users_count;
                    update_pages();
                    console.log(resp)
                },
                error(resp) {
                    console.log(resp)
                }
            })
        }




        pull_page(current_page);
        return {
            users,
            users_count,
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