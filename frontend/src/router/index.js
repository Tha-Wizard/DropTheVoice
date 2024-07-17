import HomeView from '@/views/HomeView.vue';
import SpreadView from '@/views/SpreadView.vue';
import UserLogin from "@/views/UserLogin.vue";
import UserSignUp from "@/views/UserSignUp.vue";
import UserMypage from "@/views/UserMypage.vue";
import ExampleView1 from "@/views/ExampleView1.vue";
import { createRouter, createWebHistory } from 'vue-router'
import AudioPlayView from '@/views/AudioPlayView.vue';
import PlayListVue from '@/views/PlayListVue.vue';
import RecordCHJView from '@/views/RecordCHJView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/home',
    },
    {
      path: '/home',
      name: 'home',
      component: HomeView
    },
    {
      path: '/example1',
      name: 'example1',
      component: ExampleView1,
    },
    {
      path: '/spread',
      name: 'spread',
      component: SpreadView
    },
    {
      path: "/login",
      name: "login",
      component: UserLogin,
    },
    {
      path: "/signup",
      name: "signup",
      component: UserSignUp,
    },
    {
      path: "/playlist",
      name: "playlist",
      component: PlayListVue,
    },
    {
      path: "/audioplayer",
      name: "audioplayer",
      component: AudioPlayView,
    },
    {
      path: "/mypage",
      name: "mypage",
      component: UserMypage,
    },
    {
      path: "/record",
      name: "record",
      component: RecordCHJView,
    },
  ]
})

export default router
