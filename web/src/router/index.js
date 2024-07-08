import { createRouter, createWebHistory } from 'vue-router'
import SignIn from '../views/SignIn.vue'
import MessageBoard from '../views/MessageBoard.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'MessageBoard',
      component: MessageBoard
    },
    {
      path: '/signin',
      name: 'SignIn',
      component: SignIn
    }
  ]
})

export default router
