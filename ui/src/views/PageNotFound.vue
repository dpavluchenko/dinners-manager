<template>
  <section class="page-not-found">
    <p>
      {{ $t('common.notFound') }} ({{ $route.path }})
    </p>
  </section>
</template>

<script>
export default {
  name: "PageNotFound",
  data() {
    return {};
  },
  metaInfo() {
    return {
      title: `404 ${this.$t("common.notFound")}`
    };
  }
};
</script>

<style lang="scss" scoped>
.page-not-found {
  color: #fff;
  text-align: center;
  padding-top: 50px;
}
</style>
