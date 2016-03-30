'use strict';

angular.module('volunteercrowdApp')
    .controller('AssessmentController', function ($scope, $state, Assessment, AssessmentSearch, ParseLinks) {

        $scope.assessments = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Assessment.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.assessments = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            AssessmentSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.assessments = result;
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
            $scope.assessment = {
                creationMoment: null,
                rating: null,
                comment: null,
                id: null
            };
        };
    });
