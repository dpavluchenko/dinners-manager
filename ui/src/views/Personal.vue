<template>
    <div>
        <v-tabs
                centered
                color="blue darken-1"
                dark
                class="rounded-tab"
                icons-and-text
        >
            <v-tabs-slider color='yellow'></v-tabs-slider>
            <v-tab :to="`${mainTab.route}`">
                {{mainTab.name}}
                <v-icon>{{mainTab.icon}}</v-icon>
            </v-tab>
            <v-tab v-for="item in menuItems" :key="item.route" :to="`/personal/${item.route}`">
                {{item.name}}
                <v-icon>{{item.icon}}</v-icon>
            </v-tab>
        </v-tabs>
        <router-view name="personal"></router-view>
    </div>
</template>

<script>
    export default {
        name: 'Personal',
        data() {
            return {
                mainTab:  {
                    name: 'Заказать',
                    route: '/menu',
                    icon: 'done'
                },
                menuItems: [
                    {
                        name: 'Статистика заказов',
                        route: 'orders',
                        icon: 'reorder'
                    },
                    {
                        name: 'Меню',
                        route: 'manage-menu',
                        icon: 'restaurant_menu'
                    },
                    {
                        name: 'Пользователи',
                        route: 'users',
                        icon: 'group'
                    },
                    {
                        name: 'Группы',
                        route: 'groups',
                        icon: 'group_work'
                    }
                ]
            };
        },
    }
</script>

<style scoped>
    .rounded-tab {
        border-radius: 10px;
    }
</style>