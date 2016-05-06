(function () {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('NaturalPersonFormDetailController', NaturalPersonFormDetailController);

    NaturalPersonFormDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'NaturalPersonForm', 'Gender'];

    function NaturalPersonFormDetailController($scope, $rootScope, $stateParams, DataUtils, entity, NaturalPersonForm, Gender) {
        var vm = this;
        vm.naturalPersonForm = entity;
        vm.load = function (id) {
            NaturalPersonForm.get({id: id}, function (result) {
                vm.naturalPersonForm = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:naturalPersonFormUpdate', function (event, result) {
            vm.naturalPersonForm = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
    }
})();
