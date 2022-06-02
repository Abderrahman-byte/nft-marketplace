const { alias } = require('react-app-rewire-alias');

module.exports = function override(config) {
  alias({
    '@': 'src',
    '@Components': 'src/components',
    '@Pages': 'src/pages',
    '@Context': 'src/context',
    '@Utils': 'src/utils',
    '@Styles': 'src/styles',
    '@Assets': 'src/assets'
  })(config);

  return config;
};