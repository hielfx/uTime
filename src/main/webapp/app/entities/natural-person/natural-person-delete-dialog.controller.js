(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('NaturalPersonDeleteController',NaturalPersonDeleteController);

    NaturalPersonDeleteController.$inject = ['$uibModalInstance', 'entity', 'NaturalPerson'];

    function NaturalPersonDeleteController($uibModalInstance, entity, NaturalPerson) {
        var vm = this;
        vm.naturalPerson = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            NaturalPerson.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
