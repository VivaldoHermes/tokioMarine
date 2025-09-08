<template>
  <div class="schedule">
    <!-- 1) Faixa branca com logo (link para Home) -->
    <header class="bar-logo">
      <div class="container logo-inner">
        <RouterLink to="/" class="logo-link" aria-label="Voltar para Home">
          <img class="logo" alt="Tokio Marine Seguradora" :src="TM_LOGO_URL" />
        </RouterLink>
        <div class="logo-spacer"></div>
      </div>
    </header>

    <!-- 2) Faixa degradê -->
    <div class="bar-gradient">
      <div class="container gradient-inner"><div /></div>
    </div>

    <!-- 3) Faixa larga com imagem + overlay + texto -->
    <section class="banner">
      <div class="banner-bg" />
      <div class="banner-overlay" />
      <div class="container banner-content">
        <p class="kicker">TRANSFERÊNCIAS</p>
        <h1 class="headline">Acompanhe suas transferências agendadas</h1>
        <p class="sub">Veja status, valores, datas e taxas em um só lugar.</p>
      </div>
    </section>

    <!-- 4) Lista de transferências (substitui os cards do formulário) -->
    <section class="form-wrap">
      <div class="container">
        <TransfersTable />
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { RouterLink } from 'vue-router'
import TransfersTable from '../components/TransfersTable.vue'

const TM_LOGO_URL =
  'https://www.tokiomarine.com.br/wp-content/themes/tokiomarine/images/icons/common/logo-tokiomarine.svg'
</script>

<!-- Reutiliza o mesmo CSS da página de agendamento -->
<style src="./ScheduleTransferView.css"></style>
