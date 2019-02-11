<template>
    <v-container grid-list-md>
        <v-layout row wrap>
            <v-flex xs12>
                <v-card>
                    <v-card-title class="red darken-3 white--text subheading" primary-title>
                        <span class="title">Ой!</span>
                    </v-card-title>
                    <v-card-media>
                        <v-img
                                :src="require('../assets/tomato.png')"
                                aspect-ratio="3"
                                class="grey lighten-2"
                        >
                        </v-img>
                    </v-card-media>
                    <v-card-text>
                        Похоже, Вы на неизвестной территории :) Мы еще не знаем об этой странице.
                        К счастью, мы знаем дорогу назад.
                    </v-card-text>
                    <v-card-actions>
                        <v-btn flat class="red darken-3 white--text" to="/">Назад</v-btn>
                    </v-card-actions>
                </v-card>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    export default {
        name: "PageNotFound",
        data() {
            return {};
        }
    };
</script>

<style scoped>

</style>
