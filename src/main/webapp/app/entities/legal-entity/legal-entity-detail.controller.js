(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('LegalEntityDetailController', LegalEntityDetailController);

    LegalEntityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'LegalEntity', 'AppUser'];

    function LegalEntityDetailController($scope, $rootScope, $stateParams, entity, LegalEntity, AppUser) {
        var vm = this;
        vm.legalEntity = entity;
        vm.load = function (id) {
            LegalEntity.get({id: id}, function(result) {
                vm.legalEntity = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:legalEntityUpdate', function(event, result) {
            vm.legalEntity = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
