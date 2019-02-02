<template>
    <v-app>
        <v-content>
            <app-header></app-header>
            <v-container fluid
            >
                <v-layout align-center justify-center row fill-height
                >
                    <v-flex>
                        <router-view></router-view>
                    </v-flex>
                </v-layout>
            </v-container>
            <notifications position="bottom right"></notifications>
        </v-content>
    </v-app>
</template>

<script>
    import AppHeader from './views/AppHeader'

    export default {
        username: 'App',
        components: {AppHeader},
        data() {
            return {}
        }
    }
</script>
