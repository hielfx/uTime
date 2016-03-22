'use strict';

angular.module('volunteercrowdApp')
    .controller('GenderController', function ($scope, $state, Gender, GenderSearch) {

        $scope.genders = [];
        $scope.loadAll = function() {
            Gender.query(function(result) {
               $scope.genders = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            GenderSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.genders = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.gender = {
                name: null,
                id: null
            };
        };
    });
