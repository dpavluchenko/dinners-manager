import store from '../store'

export function hasManagerRole() {
    return new Promise((resolve, reject) => {
        let userRole = store.getters.getUserRole
        if (!userRole) {
            store.dispatch('checkAuth').then(() => {
                userRole = store.getters.getUserRole
                if (userRole === 'MANAGER') resolve()
                else reject()
            }).catch(() => {
                reject()
            })
        } else {
            if (userRole === 'MANAGER') resolve()
            else reject()
        }
    })
}

export function isAuthenticated() {
    return new Promise((resolve, reject) => {
        let auth = store.getters.isAuthenticated
        if (!auth) {
            store.dispatch('checkAuth').then(() => {
                resolve()
            }).catch(() => reject())
        } else resolve(auth)
    })
}