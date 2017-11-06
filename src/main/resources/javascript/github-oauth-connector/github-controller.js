(function() {
    'use strict';

    angular.module('JahiaOAuthApp').controller('GithubController', GithubController);

    GithubController.$inject = ['$location', 'settingsService', 'helperService', 'i18nService'];

    function GithubController($location, settingsService, helperService, i18nService) {
        var vm = this;

        // Variables
        vm.expandedCard = false;
        vm.callbackUrls = [];
        vm.callbackUrl = '';

        // Functions
        vm.saveSettings = saveSettings;
        vm.goToMappers = goToMappers;
        vm.toggleCard = toggleCard;
        vm.addUrl = addUrl;
        vm.removeUrl = removeUrl;

        init();

        function saveSettings() {
            // Value can't be empty
            if (!vm.apiKey
                || !vm.apiSecret
                || vm.callbackUrls.length == 0) {
                helperService.errorToast(i18nService.message('joant_githubOAuthView.message.error.missingMandatoryProperties'));
                return false;
            }

            // the node name here must be the same as the one in your spring file
            settingsService.setConnectorData({
                connectorServiceName: 'GitHubApi',
                nodeType: 'joant:githubOAuthSettings',
                properties: {
                    isActivate: vm.isActivate,
                    apiKey: vm.apiKey,
                    apiSecret: vm.apiSecret,
                    callbackUrls: vm.callbackUrls,
                    scope: vm.scope
                }
            }).success(function() {
                vm.connectorHasSettings = true;
                helperService.successToast(i18nService.message('joant_githubOAuthView.message.succes.saveSuccess'));
            }).error(function(data) {
                helperService.errorToast(i18nService.message('joant_githubOAuthView.message.label') + ' ' + data.error);
                console.log(data);
            });
        }

        function goToMappers() {
            // the second part of the path must be the service name
            $location.path('/mappers/GitHubApi');
        }

        function toggleCard() {
            vm.expandedCard = !vm.expandedCard;
        }

        function addUrl(isValidUrl) {
            if (isValidUrl && vm.callbackUrl != '') {
                vm.callbackUrls.push(vm.callbackUrl);
                vm.callbackUrl = '';
            } else if (vm.callbackUrl != '' && !isValidUrl) {
                helperService.errorToast(i18nService.message('joant_githubOAuthView.error.callbackURL.notAValidURL'))
            }
        }

        function removeUrl(index) {
            vm.callbackUrls.splice(index, 1);
        }

        function init() {
            i18nService.addKey(githuboai18n);

            settingsService.getConnectorData('GitHubApi', ['isActivate', 'apiKey', 'apiSecret', 'callbackUrls', 'scope']).success(function(data) {
                if (data && !angular.equals(data, { })) {
                    vm.connectorHasSettings = true;
                    vm.isActivate = data.isActivate;
                    vm.apiKey = data.apiKey;
                    vm.apiSecret = data.apiSecret;
                    vm.callbackUrls = data.callbackUrls;
                    vm.scope = data.scope
                } else {
                    vm.connectorHasSettings = false;
                    vm.isActivate = false;
                }
            }).error(function(data) {
                helperService.errorToast(i18nService.message('joant_githubOAuthView.message.label') + ' ' + data.error);
            });
        }
    }
})();
