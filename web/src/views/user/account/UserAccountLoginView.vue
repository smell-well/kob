<template>
    <ContentField>
        <div class="row justify-content-center">
            <div class="col-3 ">
                <form>
                    <div class="mb-3">
                        <input type="text" class="form-control" id="username" placeholder="请输入用户名">
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
                        <input type="password" class="form-control" id="password" placeholder="请输入密码">
                        <label for="password" class="form-label">密码</label>
                    </div>
                    <div class="error_message text-center">密码错误</div>
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

export default {
    components: {
        ContentField,
    }
}
</script>

<style scoped>
.ttt {
    /* display: flex;
    justify-content: space-between; */
    text-align: justify;

}

</style>