<template>
    <v-container fluid>
        <v-layout row>
            <v-flex xs12>
                <v-card>
                    <v-card-title>
                        <v-form class="mx-4 my-4"
                                ref="form"
                                v-model="valid"
                                lazy-validation
                        >
                            <v-layout row>
                                <v-spacer></v-spacer>
                                <v-btn @click="getStat" class="mt-4" dark small flat color="green darken-1">
                                    Показать заказы
                                </v-btn>
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
                                            :rules="rules"
                                            label="Выберите дату"
                                            prepend-icon="event"
                                            readonly
                                    ></v-text-field>
                                    <v-date-picker v-model="pickerDate" @input="picker = false"></v-date-picker>
                                </v-menu>
                            </v-layout>
                        </v-form>
                    </v-card-title>
                    <v-data-table
                            hide-actions
                            disable-initial-sort
                            no-data-text="К сожалению, данных нет"
                            :headers="tableHeaders"
                            :items="tableData"
                            item-key="value"
                    >
                        <template slot="items" slot-scope="props">
                            <td class="text-xs-left">
                                {{ props.item.name}}
                            </td>
                            <td class="text-xs-center">
                                {{ props.item.FIRST_DISH}}
                            </td>
                            <td class="text-xs-center">
                                {{ props.item.GARNISH}}
                            </td>
                            <td class="text-xs-center">
                                {{ props.item.MEAT}}
                            </td>
                            <td class="text-xs-center">
                                {{ props.item.DESSERT}}
                            </td>
                        </template>
                    </v-data-table>
                </v-card>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import {types} from '../../helpers/menuTypes'
    import {handleError} from '../../helpers/errorHandler'

    export default {
        data() {
            return {
                date: null,
                pickerDate: null,
                picker: false,
                tableHeaders: [],
                tableData: [],
                valid: true,
                rules: [
                    v => !!v || 'Заполните, пожалуйста, это поле',
                ]
            }
        },
        watch: {
            pickerDate(val) {
                this.date = this.formatDate(val)
            }
        },
        methods: {
            initHeaders() {
                this.tableHeaders.push({
                    text: 'Группа',
                    align: 'left',
                    sortable: false,
                    value: 'groupName'
                })
                for (let type in types) {
                    this.tableHeaders.push({
                        text: types[type],
                        align: 'center',
                        sortable: false,
                        value: type
                    })
                }
            },
            convertData(data) {
                let stats = []
                data.statistic.forEach(val => {
                    let name = val.groupName !== 'no assigned' ? val.groupName.concat(', ', val.dinnerTime) : 'Не назначено'
                    let groupStat = val.countByType
                    groupStat['name'] = name
                    stats.push(groupStat)
                })
                let totalStat = data.total
                totalStat['name'] = 'Всего заказано'
                stats.push(totalStat)
                return stats
            },
            formatDate(date) {
                if (!date) return null
                const [year, month, day] = date.split('-')
                return `${day.padStart(2, '0')}.${month.padStart(2, '0')}.${year}`
            },
            getStat() {
                if (this.$refs.form.validate()) {
                    this.axios.get('/api/manage/stats?date='.concat(this.date))
                        .then(response => {
                            this.tableData = this.convertData(response.data)
                        })
                        .catch(error => handleError(this, error.response))
                }
            }
        },
        mounted() {
            this.date = this.formatDate(new Date().toISOString().substr(0, 10))
            this.initHeaders()
            this.getStat()
        }
    }
</script>

<style scoped>

</style>