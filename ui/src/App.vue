<template>
    <v-app>
        <v-content>
           <v-container fluid>
               <v-layout row>
                   <v-flex xs12>
                       <app-header></app-header>
                       <v-container fluid class="mt-2"
                       >
                           <v-layout align-center justify-center row
                           >
                               <v-flex>
                                   <router-view></router-view>
                               </v-flex>
                           </v-layout>
                       </v-container>
                       <notifications position="bottom right"></notifications>
                   </v-flex>
               </v-layout>
           </v-container>
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
