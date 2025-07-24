var exec = require('cordova/exec');

module.exports = {
  activateAdmin: function(success, error) {
    exec(success, error, "WarehouseDeviceAdminPlugin", "activateAdmin", []);
  },
  startKiosk: function(success, error) {
    exec(success, error, "WarehouseDeviceAdminPlugin", "startKiosk", []);
  },
  stopKiosk: function(success, error) {
    exec(success, error, "WarehouseDeviceAdminPlugin", "stopKiosk", []);
  }
};
