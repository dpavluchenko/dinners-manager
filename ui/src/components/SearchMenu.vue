<template>
    <div>
        <v-expansion-panel
                expand
        >
            <v-expansion-panel-content>
                <div slot="header">Поиск</div>
                <v-card>
                    <v-card-actions>
                        <v-container>
                            <v-layout row>
                                <v-flex md4 lg2>
                                    <v-btn @click="getCurrentMenu" slot="activator" flat dark
                                           color="light-blue accent-1">
                                        Текущее меню
                                    </v-btn>
                                </v-flex>
                                <v-spacer></v-spacer>
                            </v-layout>
                            <v-layout row>
                                <v-flex md4 lg2 class="mt-2">
                                    <v-btn @click="getMenuByDate" slot="activator" flat dark
                                           color="light-blue accent-1">
                                        Просмотреть за
                                    </v-btn>
                                </v-flex>
                                <v-flex md4 lg2>
                                    <v-menu
                                            :close-on-content-click="false"
                                            v-model="picker"
                                            :nudge-right="40"
                                            lazy
                                            transition="scale-transition"
                                            offset-y
                                            full-width
                                            min-width="290px"
                                    >
                                        <v-text-field
                                                slot="activator"
                                                v-model="date"
                                                label="Выберите дату"
                                                prepend-icon="event"
                                                readonly
                                        ></v-text-field>
                                        <v-date-picker v-model="pickerDate" locale="ru" @input="picker = false"></v-date-picker>
                                    </v-menu>
                                </v-flex>
                                <v-spacer></v-spacer>
                            </v-layout>
                        </v-container>
                    </v-card-actions>
                    <v-snackbar
                            v-model="snackbar"
                            multi-line
                            top
                    >
                        Введите дату!
                        <v-btn
                                color="pink"
                                flat
                                @click="snackbar = false"
                        >
                            Закрыть
                        </v-btn>
                    </v-snackbar>
                </v-card>
            </v-expansion-panel-content>
        </v-expansion-panel>
    </div>
</template>

<script>
    import {handleError} from '../helpers/errorHandler'

    export default {
        name: "SearchMenu",
        data() {
            return {
                snackbar: false,
                date: null,
                pickerDate: null,
                picker: false,
                url: '/api/manage/menu'
            }
        },
        watch: {
            pickerDate(val) {
                this.date = this.formatDate(val)
            }
        },
        methods: {
            formatDate(date) {
                if (!date) return null
                const [year, month, day] = date.split('-')
                return `${day.padStart(2, '0')}.${month.padStart(2, '0')}.${year}`
            },
            getCurrentMenu() {
                this.getMenu(this.url)
            },
            getMenuByDate() {
                if (this.date) {
                    this.getMenu(this.url.concat('?date=', this.date))
                } else this.snackbar = true
            },
            getMenu(url) {
                this.axios.get(url).then((response) => {
                    this.$emit('foundMenu', response.data)
                }).catch((error) => {
                    handleError(this, error.response)
                })
            }
        }
    }
</script>

<style scoped>

</style>