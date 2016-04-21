(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('AppUserDeleteController',AppUserDeleteController);

    AppUserDeleteController.$inject = ['$uibModalInstance', 'entity', 'AppUser'];

    function AppUserDeleteController($uibModalInstance, entity, AppUser) {
        var vm = this;
        vm.appUser = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            AppUser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
