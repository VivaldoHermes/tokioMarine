<script setup lang="ts">
const ICON_SCHEDULE = 'https://unpkg.com/lucide-static/icons/banknote.svg'
const ICON_LIST = 'https://unpkg.com/lucide-static/icons/credit-card.svg'
</script>

<template>
  <div class="home">
    <div class="bar-grey"></div>

    <div class="bar-gradient">
      <div class="container gradient-inner"><div /></div>
    </div>

    <div class="bar-logo">
      <div class="container logo-inner">
        <img
          class="logo"
          alt="Tokio Marine Seguradora"
          src="https://www.tokiomarine.com.br/wp-content/themes/tokiomarine/images/icons/common/logo-tokiomarine.svg"
        />
        <p class="logo-phrase">Proteção para hoje, confiança para o futuro.</p>
      </div>
    </div>

    <section class="hero">
      <div class="hero-bg" />
      <div class="hero-overlay" />
      <div class="hero-content container">
        <p class="kicker">SEGURADORA TOKIO MARINE</p>

        <h1 class="headline">
          Agende suas transferências com<br />
          praticidade e segurança.
        </h1>

        <div class="tiles-row">
          <RouterLink to="/schedule" class="tile">
            <div class="tile-top">
              <img :src="ICON_SCHEDULE" alt="Agendar" />
            </div>
            <div class="tile-bottom">
              <span class="tile-title">
                <span>Agendar</span>
                <span>Transferência</span>
              </span>
            </div>
          </RouterLink>

          <RouterLink to="/transfers" class="tile">
            <div class="tile-top">
              <img :src="ICON_LIST" alt="Transferências Agendadas" />
            </div>
            <div class="tile-bottom">
              <span class="tile-title">
                <span>Transferências</span>
                <span>Agendadas</span>
              </span>
            </div>
          </RouterLink>
        </div>
      </div>
    </section>
  </div>
</template>
<style src="./HomeView.css"></style>
