(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('AvailabilityDeleteController',AvailabilityDeleteController);

    AvailabilityDeleteController.$inject = ['$uibModalInstance', 'entity', 'Availability'];

    function AvailabilityDeleteController($uibModalInstance, entity, Availability) {
        var vm = this;
        vm.availability = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Availability.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
