<template>
    <v-toolbar dark color="primary">
        <v-toolbar-title class="white--text" v-if="userFullName">Здравствуй, {{userFullName}}</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-toolbar-items>
            <v-btn flat to="/personal" v-if="hasManagerRole">Мой кабинет</v-btn>
            <v-btn flat v-if="isAuthenticated" @click="signOut">Выйти</v-btn>
            <v-btn flat to="/sign-in" v-if="!isAuthenticated">Войти</v-btn>
            <v-btn flat to="/sign-up" v-if="!isAuthenticated">Регистрация</v-btn>
        </v-toolbar-items>
    </v-toolbar>
</template>

<script>
    import {handleError} from '../helpers/errorHandler'

    export default {
        name: "AppHeader",
        computed: {
            isAuthenticated() {
                return this.$store.getters.isAuthenticated;
            },
            hasManagerRole() {
                return this.$store.getters.getUserRole === 'MANAGER'
            },
            userFullName() {
                return this.$store.getters.getUserName;
            }
        },
        methods: {
            signOut() {
                this.$store.dispatch('signOut').then(() => {
                    this.$notify({
                        text: 'Вы успешно вышли',
                        type: 'success'
                    })
                    this.$router.push('/sign-In')
                }).catch((e) => {
                    handleError(this, e.response)
                })
            }
        }
    }
</script>

<style scoped>

</style>