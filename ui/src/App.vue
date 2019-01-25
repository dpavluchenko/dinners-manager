<template>
    <v-app>
        <v-content>
            <v-toolbar dark color="primary">
                <v-toolbar-title class="white--text"></v-toolbar-title>
                <v-spacer></v-spacer>
                <v-toolbar-items class="hidden-sm-and-down">
                    <v-btn flat to="" v-if="hasManagerRole">Мой кабинет</v-btn>
                    <v-btn flat to="" v-if="isAuthenticated">Выйти</v-btn>
                    <v-btn flat to="/sign-in" v-if="!isAuthenticated">Войти</v-btn>
                    <v-btn flat to="/sign-up" v-if="!isAuthenticated">Регистрация</v-btn>
                </v-toolbar-items>
            </v-toolbar>
            <v-container fluid
            >
                <v-layout align-center justify-center row fill-height
                >
                    <v-flex>
                        <router-view></router-view>
                    </v-flex>
                </v-layout>
            </v-container>
        </v-content>
    </v-app>
</template>

<script>

    export default {
        username: 'App',
        components: {},
        data() {
            return {}
        },
        computed: {
            isAuthenticated: () => {
                return this.$store.getters.isAuthenticated
            },
            hasManagerRole: () => {
                return this.$store.getters.getUserRole === 'MANAGER'
            }
        }
    }
</script>
