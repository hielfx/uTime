(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('AdministratorDetailController', AdministratorDetailController);

    AdministratorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Administrator', 'User', 'Incidence'];

    function AdministratorDetailController($scope, $rootScope, $stateParams, entity, Administrator, User, Incidence) {
        var vm = this;
        vm.administrator = entity;
        vm.load = function (id) {
            Administrator.get({id: id}, function(result) {
                vm.administrator = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:administratorUpdate', function(event, result) {
            vm.administrator = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
